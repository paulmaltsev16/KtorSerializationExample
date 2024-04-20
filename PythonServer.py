from flask import Flask, jsonify
from flask_cors import CORS

app = Flask(__name__)
CORS(app)

@app.route('/api', methods=['GET'])
def get_api():
    data = {
        "name": "Test api",
        "date": "2024-04-20T17:05:17Z"
    }
    return jsonify(data)

if __name__ == '__main__':
    app.run(host='192.168.27.214', port=5000, debug=True)
