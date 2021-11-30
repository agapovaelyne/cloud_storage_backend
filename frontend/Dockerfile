FROM node:16.13-alpine
RUN mkdir -p /app/front
WORKDIR /app/front
COPY package*.json ./
RUN npm install
COPY frontend/cloudKeeper-frontend .
EXPOSE 8080
RUN npm run serve
#ENTRYPOINT ["npm"]
#CMD ["run", "serve" ]