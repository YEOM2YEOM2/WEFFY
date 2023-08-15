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
import Paper from '@mui/material/Paper';
import InputBase from '@mui/material/InputBase';
import SearchIcon from '@mui/icons-material/Search';


function Participant(props) {
  let {user, participants, handleNickname } = props;
  let [ nickname, setNickname ] = useState('');
  let [ showForm, setShowForm ] = useState(false);
  let [ isFormValid, setIsFormVaild ] = useState(true);
  let [ search, setSearch ] = useState(false);
  let [ searchInput, setSearchInput ] = useState("");
  let [ searchResult, setSearchResult ] = useState([]);
  let total = [user, ...participants];

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

  const handleSearchInput = (e) => {
    let temp = e.target.value.trim();
    setSearchInput(temp);

    if (temp === "") {
      setSearch(false)
    } else {
      let result = total.filter((person) => person.getNickname().toLowerCase().includes(temp.toLowerCase()));
      setSearchResult([...result])
      setSearch(true)
    }
  }

  // Enter 키 눌렀을 때 해당 함수 실행
  const handleSubmit = (e) => {
    e.preventDefault();

  }

  if (!user) {
    return null;
  }

  
  return (
    <div style={{ color: "white" }}>
      <Paper
        component="form"
        sx={{ p: '2px 4px', display: 'flex', alignItems: 'center', height: 35, width: 302,
              m: '0 0 3px 9px'}}
        >
          <InputBase
            sx={{ ml: 1, flex: 1, fontFamily: 'NanumSquareNeo', fontSize: 13.5, fontWeight: 600 }}
            placeholder="참가자 검색"
            value={ searchInput }
            onChange={handleSearchInput}
            onKeyDown={(e) => { if (e.key === 'Enter') handleSubmit(e)}}
          />
          <SearchIcon style={{ marginRight: "10px" }} />
        </Paper>
      { search ?
        <div>
          <div>
            Search({ searchResult.length })
          </div>
          <div style={{ position: 'relative', bottom: '1px' }}>
          { searchResult.map((person, idx) => (
            <div>
              { idx > 0 ? <div className={styles.divider}></div> : null }
              <div key={idx} className={ styles.other } style={{ position: 'relative', top: '2.5px' }}>
                <span style={{ marginLeft: '10px' }}>{ person.getNickname() }</span>
                <div style={{ marginRight: '10px' }}>
                  { person.audioActive ? <MicIcon fontSize='small' />: <MicOffIcon fontSize='small' style={{ color: "#AB0C11" }}/> }
                  { person.videoActive ? <VideocamIcon fontSize='small' />: <VideocamOffIcon fontSize='small' style={{ color: "#AB0C11" }} /> }
                  </div>
              </div>
            </div>
            ))}
          </div>
        </div> : 
        <div>
        <div>
          ALL({ participants.length + 1 })
        </div>
        {/* 나 */}
        <div className={styles.me}>
          <div className="" style={{ cursor: "pointer" }}>
            {showForm ? (
                <FormControl className={ styles.nickNameForm }>
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
                <div onClick={toggleNicknameForm} style={{ position: 'relative', top: '6px' }}>
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
          <div>
            <div className={styles.divider}></div>
            <div key={idx} className={ styles.other } style={{ position: 'relative', top: '2.5px' }}>
              <span style={{ marginLeft: '10px' }}>{ person.getNickname() }</span>
              <div style={{ marginRight: '10px' }}>
                { person.audioActive ? <MicIcon fontSize='small' />: <MicOffIcon fontSize='small' style={{ color: "#AB0C11" }}/> }
                { person.videoActive ? <VideocamIcon fontSize='small' />: <VideocamOffIcon fontSize='small' style={{ color: "#AB0C11" }} /> }
                </div>
            </div>
          </div>
            
        ))}
        </div>
      </div>
      }
      
    </div>
  )
}

export default Participant;