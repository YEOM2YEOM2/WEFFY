FROM nginx:latest

# SSL 인증서 파일 복사
COPY ./ssl/fullchain.pem /etc/nginx/ssl/fullchain.pem
COPY ./ssl/privkey.pem /etc/nginx/ssl/privkey.pem

# 기존 설정 파일 복사
COPY ./nginx.conf /etc/nginx/nginx.conf

