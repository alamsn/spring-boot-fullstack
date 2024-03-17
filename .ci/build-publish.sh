# Memeriksa apakah variabel USERNAME, REPO, dan TAG telah diatur
: "${USERNAME:?USERNAME not set or empty}"
: "${REPO:?REPO not set or empty}"
: "${TAG:?TAG not set or empty}"

# Build image Docker menggunakan buildx
docker buildx build \
  --platform=linux/amd64,linux/arm64 \
  -t "${USERNAME}/${REPO}:${TAG}" \
  -t "${USERNAME}/${REPO}:latest" \
  . \
  --push \
  --build-arg api_base_url=$api_base_url
