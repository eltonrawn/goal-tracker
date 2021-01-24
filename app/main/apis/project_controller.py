from flask_restx import Namespace

api = Namespace("project", description="project related operations")
#
# @api.route('/')
# class GoalMessage(Resource):
#     @api.doc("get simple project message")
#     def get(self):
#         """khali hola priya return kore"""
#         return {"message": app.config["ES_HOST"]}, 200
#     def post(self):
#         # rs = requests.session()
#         data = request.get_json()
#         print(data)
#         return {"message": app.config["ES_HOST"]}, 200
