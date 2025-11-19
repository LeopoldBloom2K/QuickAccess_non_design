import os
from flask import Flask, request, jsonify
from pymongo import MongoClient
from dotenv import load_dotenv
from werkzeug.security import generate_password_hash, check_password_hash

# 1. .env 파일 로드 (보안 정보 가져오기)
load_dotenv()

app = Flask(__name__)

# 2. MongoDB 연결 설정
# .env 파일에 정의된 MONGO_URI 가져오기
mongo_uri = os.getenv("MONGO_URI")
client = MongoClient(mongo_uri)

# DB 및 컬렉션 선택 (없으면 자동 생성됨)
db = client.get_database("quickaccess_db")
users_collection = db.users

# --- API 라우트 정의 ---

@app.route('/')
def home():
    return "QuickAccess Server is Running!"

@app.route('/register', methods=['POST'])
def register():
    data = request.get_json()
    
    username = data.get('username')
    password = data.get('password')

    if not username or not password:
        return jsonify({"success": False, "message": "아이디와 비밀번호를 입력해주세요."}), 400

    # 이미 존재하는 유저인지 확인
    if users_collection.find_one({"username": username}):
        return jsonify({"success": False, "message": "이미 존재하는 아이디입니다."}), 409

    # 3. 비밀번호 해싱 (보안 강화) - 평문 저장 방지
    hashed_password = generate_password_hash(password)

    # DB에 저장
    new_user = {
        "username": username,
        "password": hashed_password  # 해시된 비밀번호 저장
    }
    users_collection.insert_one(new_user)

    return jsonify({"success": True, "message": "회원가입 완료"}), 201

@app.route('/login', methods=['POST'])
def login():
    data = request.get_json()
    
    username = data.get('username')
    password = data.get('password')

    # 유저 조회
    user = users_collection.find_one({"username": username})

    # 4. 비밀번호 검증 (해시 비교)
    if user and check_password_hash(user['password'], password):
        return jsonify({"success": True, "message": "로그인 성공"}), 200
    else:
        return jsonify({"success": False, "message": "아이디 또는 비밀번호가 틀렸습니다."}), 401

if __name__ == '__main__':
    # 외부 접속 허용 (안드로이드 에뮬레이터 등에서 접속 가능하도록)
    app.run(host='0.0.0.0', port=5000, debug=True)