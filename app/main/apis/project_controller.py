from flask_restx import Namespace, Resource
from flask import current_app as app
from flask import request
import requests
from flask_jwt_extended import (
    JWTManager, jwt_required, create_access_token,
    get_jwt_identity
)

from app.main.core.project_services import get_projects_by_user_id

api = Namespace("project", description="project related operations")

_http_headers = {"Content-Type": "application/json"}
project_es_index = "projects"
_es_type = "_doc"


@api.route('/')
class ProjectCRUD(Resource):
    @api.doc("crud operation for projects")
    @jwt_required
    def get(self):
        """get projects for the user"""
        try:
            project = get_projects_by_user_id(get_jwt_identity().get("id"))
        except Exception as e:
            return {"message": "Internal Server Error", "reason": str(e)}, 500
        return {"message": project}, 200

    @jwt_required
    def post(self):
        """create project for the user"""
        rs = requests.session()
        data = request.get_json()
        data["created_by"] = get_jwt_identity().get("id")
        data["total_time_spent"] = 0
        print(data)
        post_url = "http://{}/{}/{}".format(app.config["ES_HOST"], project_es_index, _es_type)
        response = rs.post(url=post_url, json=data, headers=_http_headers).json()
        return {"message": response}, 201
