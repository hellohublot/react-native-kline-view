
Pod::Spec.new do |s|
  s.name         = "RNKLineView"
  s.version      = "1.0.0"
  s.summary      = "RNKLineView"
  s.description  = <<-DESC
                  RNKLineView
                   DESC
  s.homepage     = "https://github.com/hellohublot"
  s.license      = "MIT"
  # s.license      = { :type => "MIT", :file => "FILE_LICENSE" }
  s.author             = { "hellohublot" => "hublot@aliyun.com" }
  s.platform     = :ios, "9.0"
  s.source       = { :git => "oneMore", :tag => "master" }
  s.source_files  = "Classes/**/*"
  s.requires_arc = true
  s.swift_version = "4.0"

  s.dependency "React"
  s.dependency 'lottie-ios', '~> 3.2.3'

end

  
