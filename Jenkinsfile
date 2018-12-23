
pipeline 
{
    agent
    {
        label 'master'
    }

    stages 
    {
        stage('Maven Clean')
        {
            steps
            {
                echo "Cleaning..."

                sh '''
                    mvn clean
                '''
            }
        }

        stage('Verify onebeartoe.com')
        {
            steps
            {
                echo "Verifying..."

                sh '''
                    mvn -P chrome,headless,production verify

                    ls -la web/automation/target/screenshots/
                '''
            }
        }
    }
}
