provider "aws" {
  region = "ap-northeast-2"  # 원하는 AWS 리전으로 변경
}

module "ec2_instance" {
  source = "./modules/ec2"
}

module "s3_bucket_policy" {
  source = "./modules/s3"
}