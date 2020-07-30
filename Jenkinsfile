library('pipeline@v10.8.0')

HelmPipeline {
  repository = "eu.gcr.io/neo9-software-factory/n9-images"
  gitlabConnection = "gitlab"
  k8sConfigID = "xxx-eks"
  chartVersion = "0.11.0"
  chartName = "n9/java-api"
  releaseName = "april-devteam-kpi-api"
  continuousDelivery = [
  ]
  target = "builder"
  sonarCommand = "mvn -gs ./settings.xml compile test sonar:sonar -Dsonar.projectKey=april-devteam-kpi-api -Dsonar.projectName=april-devteam-kpi-api"
  notifications = [email: 'april@neo9.fr']
}
