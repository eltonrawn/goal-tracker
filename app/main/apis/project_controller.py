from flask_restx import Namespace, Resource
from flask import current_app as app
from flask import request
import requests

api = Namespace("project", description="project related operations")

_http_headers = {"Content-Type": "application/json"}
project_es_index = "projects"
_es_type = "_doc"


@api.route('/')
class ProjectCRUD(Resource):
    @api.doc("crud operation for projects")
    # def get(self):
    #     """khali hola priya return kore"""
    #     return {"message": app.config["ES_HOST"]}, 200
    def post(self):
        """post project"""
        rs = requests.session()
        data = request.get_json()
        print(data)
        post_url = "http://{}/{}/{}".format(app.config["ES_HOST"], project_es_index, _es_type)
        response = rs.post(url=post_url, json=data, headers=_http_headers).json()
        return {"message": response}, 200
