# Memeriksa apakah variabel USERNAME, REPO, dan TAG telah diatur
: "${USERNAME:?USERNAME not set or empty}"
: "${REPO:?REPO not set or empty}"
: "${TAG:?TAG not set or empty}"

# Build image Docker menggunakan build
docker build \
  --platform=linux/amd64 \
  -t "${USERNAME}/${REPO}:${TAG}" \
  -t "${USERNAME}/${REPO}:latest" \
  . \
  --build-arg api_base_url=http://alamsn-fs.ap-southeast-1.elasticbeanstalk.com:8080

# Push image ke registry Docker
docker push "${USERNAME}/${REPO}:${TAG}"
docker push "${USERNAME}/${REPO}:latest"