/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

#include <iostream>

#include "CANController.h"

int main() { 
  std::cout << "Hello World again!\n";

  CANController canController{};

  canController.received.connect([](CANData& data){
    std::cout << "Received: " << std::hex << data.id << std::endl;
  });

  int res = canController.start("can0");

  std::cout << res << std::endl;

  std::string str;
  std::getline(std::cin, str);

  canController.stop();
  
}

