import requests
from flask import current_app as app
from requests.exceptions import HTTPError

_http_headers = {"Content-Type": "application/json"}
project_es_index = "projects"
_es_type = "_doc"


def get_project_by_id(project_id):
    rs = requests.session()
    get_url = f"http://{app.config['ES_HOST']}/{project_es_index}/_doc/{project_id}"
    response = rs.get(url=get_url)
    try:
        response.raise_for_status()
    except HTTPError as e:
        raise e
    print(response.json())
    return response.json()