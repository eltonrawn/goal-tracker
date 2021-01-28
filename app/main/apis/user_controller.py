from flask_restx import Namespace, Resource
from flask import current_app as app
from flask import request
import requests
from hashlib import md5
from app.main.commons.json_helpers import validate_json

api = Namespace("user", description="user related operations")

_http_headers = {"Content-Type": "application/json"}
user_es_index = "users"
_es_type = "_doc"


@api.route('/')
class UserCRUD(Resource):
    @api.doc("crud operation for projects")
    # def get(self):
    #     """khali hola priya return kore"""
    #     return {"message": app.config["ES_HOST"]}, 200
    def post(self):
        """create user"""
        rs = requests.session()
        data = request.get_json()

        try:
            user_data = validate_json(data, ["username", "password"])
            user_data["password"] = md5(
                user_data["password"].encode(encoding="utf-8")
            ).hexdigest()
            if "user_role" not in user_data:
                user_data["user_role"] = "user"
        except Exception as e:
            # app.logger.warning("Bad request")
            return e, 400

        search_url = f"http://{app.config['ES_HOST']}/{user_es_index}/_doc/_search"
        query_params = {"query": {"bool": {"must": [{"match": {"username": user_data["username"]}}]}}}
        response = rs.post(url=search_url, json=query_params, headers=_http_headers).json()

        print(response)
        if "hits" in response:
            if response["hits"]["total"]["value"] >= 1:
                app.logger.info("Username already exists")
                return "username already exists", 200

        post_url = f"http://{app.config['ES_HOST']}/{user_es_index}/_doc"
        response = rs.post(url=post_url, json=user_data, headers=_http_headers).json()

        if "result" in response:
            if response["result"] == "created":
                app.logger.info("Create user API completed")
                return response["_id"], 201
        return response, 500
