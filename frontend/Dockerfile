FROM node:18-alpine AS build
WORKDIR /app
COPY package.json .
RUN npm install
COPY . .
RUN rm -rf node_modules/.cache && npm install esbuild@0.24.2
RUN npm run build

FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
# Thêm chứng chỉ SSL và khóa riêng
COPY ssl/hcmute-care.id.vn.pem /etc/nginx/ssl/hcmute-care.id.vn.pem
COPY ssl/hcmute-care.id.vn.key /etc/nginx/ssl/hcmute-care.id.vn.key
EXPOSE 80 443
CMD ["nginx", "-g", "daemon off;"]