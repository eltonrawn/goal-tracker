from flask_restx import Namespace, Resource
from flask import current_app as app
from flask import request
import requests
from hashlib import md5
from app.main.commons.json_helpers import validate_json

from flask_jwt_extended import (
    create_access_token,
    create_refresh_token
)

api = Namespace("auth", description="auth related operations")

_http_headers = {"Content-Type": "application/json"}
user_es_index = "users"
_es_type = "_doc"


@api.route("/login")
class Login(Resource):
    def post(self):
        rs = requests.session()
        auth_data = request.get_json()
        try:
            auth_data = validate_json(auth_data, ["username", "password"])
            auth_data["password"] = md5(
                auth_data["password"].encode(encoding="utf-8")
            ).hexdigest()
        except Exception as e:
            # app.logger.warning("Bad request")
            return e, 400

        username = auth_data["username"]
        password = auth_data["password"]

        auth_url = "http://{}/{}/{}/_search".format(
            app.config["ES_HOST"], user_es_index, _es_type
        )
        auth_query = {
            "_source": ["username", "user_role"],
            "query": {
                "bool": {
                    "must": [
                        {"match": {"username": username}},
                        {"match": {"password": password}}
                    ]
                }
            },
        }
        response = rs.post(url=auth_url, json=auth_query, headers=_http_headers).json()

        print(response)

        if "hits" in response:
            if response["hits"]["total"]["value"] == 1:
                data = response["hits"]["hits"][0]["_source"]
                data["id"] = response["hits"]["hits"][0]["_id"]
                jwt_data = {
                    "id": data["id"],
                    "username": data["username"],
                    "user_role": data["user_role"]
                }
                data["access_token"] = create_access_token(identity=jwt_data)
                # data["refresh_token"] = create_refresh_token(identity=jwt_data)
                return data, 200
            else:
                app.logger.info("Unauthorized")
                return {"message": "unauthorized"}, 401
        return response, 500