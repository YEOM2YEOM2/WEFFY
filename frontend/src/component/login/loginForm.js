import * as React from 'react';

import styles from './loginForm.module.css';

import Button from '@mui/material/Button';
import Form from 'react-bootstrap/Form';

function loginForm() {
  return (
    <div className={styles.login_form_wrapper}>
      <div className={styles.login_form} style={{ position: 'relative', zIndex: 0 }}>
        <p className={styles.logo} style={{ position: 'relative', zIndex: 1 }}>WEEFY</p>
        <Form className={styles.custom_form}  style={{ position: 'relative', zIndex: 1 }}>
          <Form.Group className="mb-3" controlId="exampleForm.ControlInput">
            <Form.Label style={{marginLeft: '4px'}}>Email</Form.Label>
            <Form.Control type="email" placeholder="Mattermost Email" />
          </Form.Group>
          <Form.Group className="mb-3" controlId="exampleForm.ControlPassword">
            <Form.Label style={{marginLeft: '4px'}}>Password</Form.Label>
            <Form.Control type="password" placeholder="Mattermost Password" />
          </Form.Group>
          <div className={styles.forgot_password}>
            <Button variant="text" href="https://meeting.ssafy.com/reset_password" style={{ textDecorationLine: 'none', color: 'black', fontFamily: 'Poppins' }}>Forgot Password?</Button>
          </div>
          <Button variant="contained" size="large" className={styles.login_btn} style={{ fontSize: '20px', fontFamily: 'Poppins', fontWeight: '500', backgroundColor: '#2672B9' }}>Sign in</Button>
          <div className={styles.sign}>
            <p style={{ marginBottom: '0', marginTop: '9px' }}>You don't have a account?</p><Button variant="text" href="https://meeting.ssafy.com/reset_password" style={{ textDecorationLine: 'none', color: '#2672B9', fontFamily: 'Poppins', fontSize: '17px' }}>Sign up</Button>
          </div>
        </Form>
      </div>
    </div>
  )
}

export default loginForm