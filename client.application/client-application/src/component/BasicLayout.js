import React from 'react';
import PageStackContainer from './ui/PageStackContainer';
import { Outlet } from 'react-router-dom';

function BasicLayout({header}) {
    
  return (
    <div id='basic-layout'>
      {header}

      <PageStackContainer>
          <Outlet/>
      </PageStackContainer>
    </div>
  )
}

export default BasicLayout;