FROM node:14.15.0-alpine
RUN mkdir -p /app/front
WORKDIR /app/front
COPY package*.json ./
RUN npm install
COPY frontend/cloudKeeper-frontend .
EXPOSE 8080
CMD ["npm", "run", "serve"]