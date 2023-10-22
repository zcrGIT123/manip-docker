FROM ubuntu:20.04 as build-step

RUN rm /bin/sh && ln -s /bin/bash /bin/sh

WORKDIR /usr/app

COPY . /usr/app

RUN apt-get update && \
    apt-get install -y curl && \
    curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.3/install.sh | bash && \
    export NVM_DIR="$HOME/.nvm" && \
    [ -s "$NVM_DIR/nvm.sh" ] && \. "$NVM_DIR/nvm.sh" && \
    nvm install 18 && \
    nvm use 18 && \
    npm install -g yarn && \
    npm install -g @angular/cli && \
    cd /usr/app  && \
    npm install --force && \
    npm run build


FROM nginx:1.20.1
COPY --from=build-step /usr/app/dist/frontend /usr/share/nginx/html
EXPOSE 80