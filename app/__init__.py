from flask_restx import Api
from flask import Blueprint
from app.main.config import Config

# from .main.controller.user_controller import api as user_ns
# from .main.controller.auth_controller import api as auth_ns
from app.main.apis.goal_controller import api as goal_ns
from app.main.apis.project_controller import api as project_ns
from app.main.apis.user_controller import api as user_ns

blueprint = Blueprint('api', Config.APPNAME, url_prefix="/goal-tracker")

api = Api(blueprint,
          title='FLASK RESTPLUS(RESTX) API BOILER-PLATE WITH JWT',
          version='1.0',
          description='a boilerplate for flask restplus (restx) web service'
          )

api.add_namespace(goal_ns, path='/goals')
api.add_namespace(project_ns, path='/projects')
api.add_namespace(user_ns, path='/users')
# api.add_namespace(auth_ns)