# Build Stage
FROM ubuntu:20.04 AS build

# 기본 도구 및 NodeSource 저장소 추가
RUN apt-get update && apt-get install -y curl
RUN curl -sL https://deb.nodesource.com/setup_14.x | bash -

# Node.js 14.17.0 설치
RUN apt-get install -y nodejs=14.17.0-1nodesource1 && apt-mark hold nodejs

# 앱 디렉터리 설정
WORKDIR /app

# 앱 종속성 설치 및 빌드
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

# Run Stage
FROM nginx:alpine

# 앱 빌드 결과물 복사
COPY --from=build /app/build /usr/share/nginx/html

# nginx 설정 복사 (필요하다면)
# COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
