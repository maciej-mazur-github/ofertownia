docker build -t ofertownia .
docker stop ofertownia || true
docker rm ofertownia || true
docker run -d -p 8080:8080 --name ofertownia ofertownia