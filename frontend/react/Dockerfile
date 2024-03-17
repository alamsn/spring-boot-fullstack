FROM node:19-alpine
ARG api_base_url
WORKDIR /app
COPY package*.json .
RUN npm i --silent
COPY . .
RUN echo "VITE_API_BASE_URL=${api_base_url}" > .env
# ENV VITE_API_BASE_URL=http://alamsn-fs.ap-southeast-1.elasticbeanstalk.com:8080
EXPOSE 5173
CMD ["npm", "run", "dev"]