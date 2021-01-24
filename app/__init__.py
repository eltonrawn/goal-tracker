from flask_restx import Api
from flask import Blueprint
from app.main.config import Config

# from .main.controller.user_controller import api as user_ns
# from .main.controller.auth_controller import api as auth_ns
from .main.apis.goal_controller import api as goal_ns

blueprint = Blueprint('api', Config.APPNAME, url_prefix="/goal-tracker")

api = Api(blueprint,
          title='FLASK RESTPLUS(RESTX) API BOILER-PLATE WITH JWT',
          version='1.0',
          description='a boilerplate for flask restplus (restx) web service'
          )

api.add_namespace(goal_ns, path='/goals')
# api.add_namespace(auth_ns)