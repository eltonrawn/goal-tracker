from flask_restx import Namespace, Resource
from flask import current_app as app
from flask import request
import requests
from flask_jwt_extended import (
    JWTManager, jwt_required, create_access_token,
    get_jwt_identity
)

from app.main.core.project_services import get_project_by_id

api = Namespace("goal", description="goal related operations")

_http_headers = {"Content-Type": "application/json"}
goal_es_index = "goals"
_es_type = "_doc"


@api.route('/')
class GoalCRUD(Resource):
    @api.doc("crud operation for goals")
    # def get(self):
    #     """get goals"""
    #     return {"message": app.config["ES_HOST"]}, 200
    @jwt_required
    def post(self):
        """post goal"""
        try:
            rs = requests.session()
            data = request.get_json()
            print(data)
            data["created_by"] = get_jwt_identity().get("id")
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

