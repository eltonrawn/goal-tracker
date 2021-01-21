from flask_restx import Namespace, Resource

api = Namespace("goal", description="goal related operations")


@api.route('/priya')
class GoalMessage(Resource):
    @api.doc("get simple goal message")
    def get(self):
        return {"message": "hola priya"}, 200
