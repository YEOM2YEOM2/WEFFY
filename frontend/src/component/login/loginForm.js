import * as React from 'react';

import styles from './loginForm.module.css'

import Form from 'react-bootstrap/Form';

function loginForm() {
  return (
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
      </Form>
    </div>
  )
}

export default loginForm