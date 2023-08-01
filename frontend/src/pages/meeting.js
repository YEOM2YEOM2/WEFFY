import React from 'react';
import styles from './meeting.module.css'

import LeftSide from '../component/meeting/leftSide';
import RightSide from '../component/meeting/rightSide';

import Grid from '@mui/material/Grid';


function Meeting() {
  return (
    <div>
      <Grid container>
        <Grid item lg={9}>
          <LeftSide className={styles.temp}/>
        </Grid>
        <Grid item lg={3}>
          <RightSide />
        </Grid>
      </Grid>
    </div>
  )
}

export default Meeting