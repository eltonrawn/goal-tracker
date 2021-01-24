from flask_restx import Namespace, Resource
from flask import current_app as app
from flask import request
import requests

api = Namespace("goal", description="goal related operations")

_http_headers = {"Content-Type": "application/json"}
_es_index = "goals"
_es_type = "_doc"

@api.route('/')
class GoalMessage(Resource):
    @api.doc("get simple goal message")
    def get(self):
        """get goals"""
        return {"message": app.config["ES_HOST"]}, 200

    def post(self):
        """post goal"""
        rs = requests.session()
        data = request.get_json()
        print(data)
        post_url = "http://{}/{}/{}".format(app.config["ES_HOST"], _es_index, _es_type)
        # http://localhost:9200/goals/_doc
        response = rs.post(url=post_url, json=data, headers=_http_headers).json()
        return {"message": response}, 200
