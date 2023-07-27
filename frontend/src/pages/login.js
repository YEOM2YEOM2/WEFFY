import React from 'react';
import styles from './login.module.css'

import LoginForm from '../component/login/loginForm.js';

import Moon from '../assets/images/Moon_slice.png';
import Star from '../assets/images/raydown.png';
import People from '../assets/images/3d_people_all.png';

function login() {
  return (
    <div className={styles.login}>
        <div className={styles.moon_container}>
            <img className={styles.moon} src={ Moon } alt="Moon" />
        </div>
        <div>
            <img className={styles.star1} src={ Star } alt="Star" />
            <img className={styles.star2} src={ Star } alt="Star" />
            <img className={styles.star3} src={ Star } alt="Star" />
        </div>
        <div className="">
            <LoginForm />
            <img className={styles.people} src={ People } alt="people" />
        </div>
    </div>
  )
}

export default login;