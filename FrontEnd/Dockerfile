FROM nginx:1.27


RUN rm /etc/nginx/conf.d/default.conf

COPY ./dist/ /usr/share/nginx/html
COPY ./nginx/app.conf.template /etc/nginx/templates/app.conf.template


