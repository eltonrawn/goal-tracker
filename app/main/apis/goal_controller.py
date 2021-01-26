from flask_restx import Namespace, Resource
from flask import current_app as app
from flask import request
import requests
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

    def post(self):
        """post goal"""
        try:
            rs = requests.session()
            data = request.get_json()
            print(data)
            if "project_id" not in data:
                raise Exception("project_id must be included in goal")

            if get_project_by_id(data["project_id"]) is None:
                raise Exception("project_id must be valid")

            post_url = "http://{}/{}/{}".format(app.config["ES_HOST"], goal_es_index, _es_type)
            response = rs.post(url=post_url, json=data, headers=_http_headers).json()
            return {"message": response}, 200
        except Exception as e:
            return {"message": "Internal Server Error", "reason": str(e)}, 500

