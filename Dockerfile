FROM adoptopenjdk/openjdk11:alpine-jre
LABEL authors="roadToSomething"


ENTRYPOINT ["top", "-b"]