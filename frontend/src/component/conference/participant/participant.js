import React from 'react'
import { useState } from 'react';
import styles from './participant.module.css';

// mui
import MicIcon from '@mui/icons-material/Mic';
import MicOffIcon from '@mui/icons-material/MicOff';
import VideocamIcon from '@mui/icons-material/Videocam';
import VideocamOffIcon from '@mui/icons-material/VideocamOff';
import HighlightOffIcon from '@mui/icons-material/HighlightOff';
import FormControl from '@mui/material/FormControl';
import FormHelperText from '@mui/material/FormHelperText';
import Tooltip from '@mui/material/Tooltip';
import Input from '@mui/material/Input';
import InputLabel from '@mui/material/InputLabel';
import IconButton from '@mui/material/IconButton';
import InfoIcon from '@mui/icons-material/Info';


function Participant(props) {
  let {user, participants, handleNickname } = props;
  let [ nickname, setNickname ] = useState('');
  let [ showForm, setShowForm ] = useState(false);
  let [ isFormValid, setIsFormVaild ] = useState(true);

  const handleChange = (e) => {
    setNickname(e.target.value);
    e.preventDefault();
  }

  const toggleNicknameForm = () => {
    setShowForm(!showForm)
  }

  const handlePressKey = (e) => {
    if (e.key === 'Enter') {
      if (nickname.length >= 3 && nickname.length <= 20) {
        handleNickname(nickname);
        toggleNicknameForm();
        setIsFormVaild(true);
      } else {
        setIsFormVaild(false);
      }
    }
  }

  if (!user) {
    return null;
  }

  
  return (
    <div style={{ color: "white" }}>
      <div>
        ALL()
      </div>
      {/* 나 */}
      <div className={styles.me}>
        <div className="" style={{ cursor: "pointer" }}>
          {showForm ? (
              <FormControl className={ styles.nickNameForm } style={{  }}>
                  <IconButton color="inherit" id="closeButton" onClick={toggleNicknameForm} style={{postion: 'absolute', top: '-7px'}}>
                      <HighlightOffIcon />
                  </IconButton>
                  <InputLabel htmlFor="name-simple" id="label" style={{ fontFamily: 'NanumSquareNeo', top: '10px', left: '-13px' }}>
                      참가자명 수정
                  </InputLabel>
                  <Input
                      id="input"
                      value={nickname}
                      onChange={handleChange}
                      onKeyPress={handlePressKey}
                      required
                      style={{ fontFamily: 'NanumSquareNeo' }}
                  />
                  {!isFormValid && nickname.length <= 3 && (
                      <FormHelperText id="name-error-text" style={{ fontFamily: 'GmarketSans' }}>4 ~ 20자로 작성해주세요.</FormHelperText>
                  )}
                  {!isFormValid && nickname.length >= 21 && (
                      <FormHelperText id="name-error-text" style={{ fontFamily: 'GmarketSans' }}>4 ~ 20자로 작성해주세요.</FormHelperText>
                  )}
              </FormControl>
          ) : (
              <div onClick={toggleNicknameForm}>
                  <Tooltip title="참가자명 변경" placement='right'>
                      <span id="nickname" style={{ fontSize: '13px', marginLeft: '10px' }}>{user.getNickname()}</span>
                      {user.isLocal() && <span id=""><InfoIcon fontSize='small' style={{ marginLeft: '3px' }} /></span>}
                  </Tooltip>
              </div>
          )}
        </div>
        <div className={ styles.meSetting }>
          <span className={ styles.roleMe }>Me</span>
          <div style={{ position: 'relative', top: '3px' }}>
            { user.audioActive ? <MicIcon fontSize='small' />: <MicOffIcon fontSize='small' style={{ color: "#AB0C11" }} /> }
            { user.videoActive ? <VideocamIcon fontSize='small' />: <VideocamOffIcon fontSize='small' style={{ color: "#AB0C11" }} /> }
          </div>
        </div>
      </div>
      {/* 다른 사용자 */}
      <div style={{ position: 'relative', bottom: '1px' }}>
      { participants.map((person, idx) => (
          <div key={idx} className={ styles.other }>
            <span style={{ marginLeft: '10px' }}>{ person.getNickname() }</span>
            <div style={{ marginRight: '10px' }}>
              { person.audioActive ? <MicIcon fontSize='small' />: <MicOffIcon fontSize='small' style={{ color: "#AB0C11" }}/> }
              { person.videoActive ? <VideocamIcon fontSize='small' />: <VideocamOffIcon fontSize='small' style={{ color: "#AB0C11" }} /> }
              </div>
          </div>
      ))}
      </div>
    </div>
  )
}

export default Participant;