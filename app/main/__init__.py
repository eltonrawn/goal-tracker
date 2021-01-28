# from flask import Flask
# from flask_sqlalchemy import SQLAlchemy
# from flask_bcrypt import Bcrypt

from app.main.config import instances
from flask.app import Flask
from app.main.config import Config
# db = SQLAlchemy()
# flask_bcrypt = Bcrypt()

from flask_jwt_extended import (
    JWTManager, jwt_required, create_access_token,
    get_jwt_identity
)


def create_app(instance_name: str) -> Flask:
    app = Flask(__name__)
    app.config.from_object(instances[instance_name])
    app.config.from_pyfile(f"{Config.BASEDIR}/config-{instance_name}.cfg", silent=True)
    # db.init_app(app)
    # flask_bcrypt.init_app(app)

    app.config['JWT_SECRET_KEY'] = 'super-secret'  # Change this!
    jwt = JWTManager(app)

    return app
