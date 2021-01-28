import requests
from flask import current_app as app
from requests.exceptions import HTTPError

_http_headers = {"Content-Type": "application/json"}
goal_es_index = "goals"
_es_type = "_doc"


def get_goal_by_id(goal_id):
    rs = requests.session()
    get_url = f"http://{app.config['ES_HOST']}/{goal_es_index}/_doc/{goal_id}"
    response = rs.get(url=get_url)
    try:
        response.raise_for_status()
    except HTTPError as e:
        raise e
    print(response.json())
    return response.json()["_source"]

def get_goals_by_user_id(user_id):
    rs = requests.session()
    search_url = f"http://{app.config['ES_HOST']}/{goal_es_index}/_doc/_search"
    query_params = {"query": {"bool": {"must": [{"match": {"created_by": user_id}}]}}}
    response = rs.post(url=search_url, json=query_params, headers=_http_headers)
    # print(response.json())
    if response.json() is None or "hits" not in response.json():
        raise Exception("elasticsearch error")

    return response.json()["hits"]["hits"]