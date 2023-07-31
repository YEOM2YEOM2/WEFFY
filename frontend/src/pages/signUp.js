import styles from './signUp.module.css';

import Grid from '@mui/material/Grid';
import Hidden from '@mui/material/Hidden';

import SignupWide from '../component/signup/signupWide';
import SignupNarrow from '../component/signup/signupNarrow';

import Plane from './../assets/images/signup_plane.png';

function SignUp() {
  return (
    <Grid container className={styles.signup}>
      <Hidden mdDown>
        <Grid item md={5} xs={0} className={styles.left_box}>
          <div className={styles.description}>
            <p className={styles.logo}>WEFFY</p>
            <p className={styles.join}>Join with us</p>
          </div>
          <img src={Plane} alt="plane" style={{ position: 'relative', zIndex: 1 }} className={styles.img}></img>
        </Grid>
      </Hidden>
      <Grid item md={7} className={styles.right_box}>
        <Hidden mdDown>
          <SignupWide/>
        </Hidden>
        <Hidden mdUp>
          <SignupNarrow style={{ width: '100vw' }}/>
        </Hidden>
      </Grid>
    </Grid>
  )
}

export default SignUp