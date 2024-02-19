-include .env
export

gradle=./gradlew

env:
	@ printf "ARTIFACTORY_USER=$(artifactory.user)\nARTIFACTORY_PASS=$(artifactory.pass)\n" > .env

clean:
	@ $(gradle) clean --quiet

test: clean
	@ $(gradle) test

jar: clean
	@ $(gradle) bootJar

coverage: clean
	@ $(gradle) test jacocoTestReport jacocoTestCoverageVerification

mutation: clean
	@ $(gradle) pitest

tag:
	@ git checkout develop
	@ git merge master
	@ git tag release-v$(version)
	@ git push origin develop
	@ git push --tags
	@ git checkout master
	@ git merge release-v$(version)
	@ git push origin master
	@ git checkout develop

tag-fix:
	@ git checkout $(branch)
	@ git merge master
	@ git tag release-v$(version)
	@ git push origin $(branch)
	@ git push --tags
	@ git checkout master
	@ git merge release-v$(version)
	@ git push origin master
	@ git checkout $(branch)