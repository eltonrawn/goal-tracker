from flask_restx import Namespace, Resource
from flask import current_app as app
from flask import request
import requests
from flask_jwt_extended import (
    JWTManager, jwt_required, create_access_token,
    get_jwt_identity
)

from app.main.core.project_services import get_project_by_id
from app.main.core.goal_services import (get_goal_by_id, get_goals_by_user_id)

api = Namespace("goal", description="goal related operations")

_http_headers = {"Content-Type": "application/json"}
goal_es_index = "goals"
project_es_index = "projects"
_es_type = "_doc"


@api.route('/')
class GoalCRUD(Resource):
    @api.doc("crud operation for goals")
    @jwt_required
    def get(self):
        """get goals"""
        try:
            project = get_goals_by_user_id(get_jwt_identity().get("id"))
        except Exception as e:
            return {"message": "Internal Server Error", "reason": str(e)}, 500
        return {"message": project}, 200

    @jwt_required
    def post(self):
        """post goal"""
        try:
            rs = requests.session()
            data = request.get_json()
            print(data)
            data["created_by"] = get_jwt_identity().get("id")
            data["spent_duration"] = 0
            if "project_id" not in data:
                raise Exception("project_id must be included in goal")

            project = get_project_by_id(data["project_id"])["_source"]
            print(project)
            if project is None:
                raise Exception("project_id must be valid")

            if "created_by" not in project:
                raise Exception("this project has no user access")
            if project["created_by"] != data["created_by"]:
                raise Exception("user has no access of this project")

            post_url = "http://{}/{}/{}".format(app.config["ES_HOST"], goal_es_index, _es_type)
            response = rs.post(url=post_url, json=data, headers=_http_headers).json()
            return {"message": response}, 201
        except Exception as e:
            return {"message": "Internal Server Error", "reason": str(e)}, 500


@api.route('/duration')
class GoalDuration(Resource):
    @api.doc("goal-duration related operations")
    @jwt_required
    def put(self):
        """update duration"""
        try:
            rs = requests.session()
            data = request.get_json()
            data["created_by"] = get_jwt_identity().get("id")

            goal = get_goal_by_id(data["goal_id"])
            if "created_by" not in goal:
                raise Exception("this project has no user access")
            if goal["created_by"] != data["created_by"]:
                raise Exception("user has no access of this project")

            # post_url = "http://{}/{}/{}".format(app.config["ES_HOST"], goal_es_index, _es_type)
            post_url = f"http://{app.config['ES_HOST']}/{goal_es_index}/_update/{data['goal_id']}"
            count_field = "spent_duration"
            update_params = {"script": {"source": f"ctx._source.{count_field} += {data['spent_duration']}"}}
            response = rs.post(url=post_url, json=update_params, headers=_http_headers).json()
            # print(goal)

            post_url = f"http://{app.config['ES_HOST']}/{project_es_index}/_update/{goal['project_id']}"
            count_field = "total_time_spent"
            update_params = {"script": {"source": f"ctx._source.{count_field} += {data['spent_duration']}"}}
            response = rs.post(url=post_url, json=update_params, headers=_http_headers).json()

        except Exception as e:
            return {"message": "Internal Server Error", "reason": str(e)}, 500
        return {"message": response}, 200
