// jobs-dsl/POC_pipeline.groovy

// 1. กำหนดลิสต์โปรเจคที่อยู่คนละ repo
def projects = [
  [ name: 'project-a',
    gitUrl: 'https://github.com/padstrike/example-pipeline.git' ],
  [ name: 'project-b',
    gitUrl: 'https://github.com/padstrike/example-pipeline.git' ],
  // เพิ่มเติมตามต้องการ
]

def credId = 'gitlab-token'
def branch = 'main'
def envFiles = ['dev.Jenkinsfile','sit.Jenkinsfile','uat.Jenkinsfile']

projects.each { proj ->
  envFiles.each { fileName ->
    def envName = fileName.replace('.Jenkinsfile','')      // dev, sit, uat
    def jobName = "${proj.name}-${envName}"

    // สร้าง Pipeline Job ใหม่
    pipelineJob(jobName) {
      description("Auto-generated: ${proj.name} [${envName}]")
      definition {
        cpsScm {
          scm {
            git {
              remote {
                url(proj.gitUrl)
                credentials(credId)
              }
              branches("*/${branch}")
            }
          }
          scriptPath(fileName)  // Jenkinsfile แต่ละ env อยู่ที่ root ของโปรเจค repo
        }
      }
    }
  }
}
