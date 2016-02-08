# Private class
class komea_foundation::install inherits komea_foundation {
  if $caller_module_name != $module_name {
    fail("Use of private class ${name} by ${caller_module_name}")
  }

  tomcat::war { 'komea.war':
    catalina_base => '/var/lib/tomcat8',
    war_source => "puppet:///modules/${module_name}/komea.war"
  }  
}
