from flask_restx import Namespace, Resource
from flask import current_app as app
from flask import request
import requests

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
        rs = requests.session()
        data = request.get_json()
        print(data)
        post_url = "http://{}/{}/{}".format(app.config["ES_HOST"], goal_es_index, _es_type)
        response = rs.post(url=post_url, json=data, headers=_http_headers).json()
        return {"message": response}, 200
