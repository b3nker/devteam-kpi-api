library('pipeline@refactor/docker-image-squash')

HelmPipeline {
  repository = "eu.gcr.io/neo9-software-factory/n9-images"
  gitlabConnection = "gitlab"
  k8sConfigID = "xxx-eks"
  chartVersion = "0.11.0"
  chartName = "n9/java-api"
  releaseName = "april-devteam-kpi"
  continuousDelivery = [
  ]
  target = "builder"
  sonarCommand = "mvn -gs ./settings.xml compile test sonar:sonar"
  notifications = [email: 'xavier.michel@neo9.fr']
}
