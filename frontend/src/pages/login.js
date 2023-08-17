import styles from "./login.module.css";

import LoginForm from "../component/login/loginForm.js";

import Moon from "../assets/images/Moon_slice.png";
import Star from "../assets/images/raydown.png";
import People from "../assets/images/3d_people_all.png";

import Grid from "@mui/material/Grid";
import Hidden from "@mui/material/Hidden";

function Login() {
  return (
    <div className={styles.login} style={{ position: "relative", zIndex: 0 }}>
      <div className={styles.moon_container}>
        <img className={styles.moon} src={Moon} alt="Moon" />
      </div>
      <div>
        <img className={styles.star1} src={Star} alt="Star" />
        <img className={styles.star2} src={Star} alt="Star" />
        <img className={styles.star3} src={Star} alt="Star" />
      </div>

      <Grid container spacing={1}>
        <Grid item md={6} xs={12} style={{ position: "relative", zIndex: 1 }}>
          <LoginForm />
        </Grid>
        <Hidden mdDown>
          <Grid
            item
            md={6}
            className={styles.img_container}
            style={{ padding: "0" }}
          >
            <img className={styles.people} src={People} alt="people" />
          </Grid>
        </Hidden>
      </Grid>
    </div>
  );
}

export default Login;
