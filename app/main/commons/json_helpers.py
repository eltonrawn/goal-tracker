
def validate_json(json_data, mandatory_fields):
    for key, value in json_data.items():
        if key in mandatory_fields and not value:
            raise KeyError("Mandatory field missing")
    return json_data