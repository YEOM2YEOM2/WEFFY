import React from 'react';
import styles from './signupWide.module.css'

import Button from '@mui/material/Button';
import Box from '@mui/material/Box';
import IconButton from '@mui/material/IconButton';
import Input from '@mui/material/Input';
import FilledInput from '@mui/material/FilledInput';
import OutlinedInput from '@mui/material/OutlinedInput';
import InputLabel from '@mui/material/InputLabel';
import InputAdornment from '@mui/material/InputAdornment';
import FormHelperText from '@mui/material/FormHelperText';
import FormControl from '@mui/material/FormControl';
import TextField from '@mui/material/TextField';
import Visibility from '@mui/icons-material/Visibility';
import VisibilityOff from '@mui/icons-material/VisibilityOff';


import Form from 'react-bootstrap/Form';
import Container from 'react-bootstrap/Container';
import { useNavigate } from 'react-router-dom';

function SignupWide() {
    
    let navigate = useNavigate();

    const [showPassword, setShowPassword] = React.useState(false);

    const handleClickShowPassword = () => setShowPassword((show) => !show);

    const handleMouseDownPassword = (event) => {
        event.preventDefault();
    };

    return (
    <div>
        <p className={styles.title}>Create Account</p>
        <div>
        <FormControl sx={{ m: 1, width: '25ch' }} variant="outlined">
          <InputLabel htmlFor="outlined-adornment-password">Password</InputLabel>
          <OutlinedInput
                id="outlined-adornment-password"
                type={showPassword ? 'text' : 'password'}
                endAdornment={
                <InputAdornment position="end">
                    <IconButton
                    aria-label="toggle password visibility"
                    onClick={handleClickShowPassword}
                    onMouseDown={handleMouseDownPassword}
                    edge="end"
                    >
                    {showPassword ? <VisibilityOff /> : <Visibility />}
                    </IconButton>
                </InputAdornment>
                }
                label="Password"
            />
            </FormControl>
        </div>
        {/* <Form className={styles.custom_form}  style={{ position: 'relative', zIndex: 1 }}>
            <Form.Group className="mb-3" controlId="exampleForm.ControlInput">
            <Form.Label style={{marginLeft: '4px'}}>Email</Form.Label>
            <Form.Control type="email" placeholder="Mattermost Email" />
            </Form.Group>
            <Form.Group className="mb-3" controlId="exampleForm.ControlPassword">
            <Form.Label style={{marginLeft: '4px'}}>Password</Form.Label>
            <Form.Control type="password" placeholder="Mattermost Password" />
            </Form.Group>
            <Form.Group className="mb-3" controlId="exampleForm.ControlPassword">
            <Form.Label style={{marginLeft: '4px'}}>Password Confirmation</Form.Label>
            <Form.Control type="password" placeholder="Password Confirmation" />
            </Form.Group>
        </Form> */}
        <Button variant="contained" size="large" 
        className={styles.createBtn} style={{ fontFamily: 'HindGunturBold', backgroundColor: '#2672B9', paddingTop: '10px' }}
        onClick={()=> {navigate('/')}}>
            Create Account
        </Button>
    </div>
    )
}

export default SignupWide