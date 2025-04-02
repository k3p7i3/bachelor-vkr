import React from 'react';
import PageStackContainer from './PageStackContainer';

function BasicLayout({header, content}) {
    
  return (
    <div id='basic-layout'>
      {header}

      <PageStackContainer>
        {content}
      </PageStackContainer>
    </div>
  )
}

export default BasicLayout;