import React, { useState, useRef } from 'react';
import Picker from 'emoji-picker-react';
import { Input, Button } from 'antd';
import { SmileOutlined, FileImageOutlined, SendOutlined } from '@ant-design/icons';

const { TextArea } = Input;
const EmojiPickerInput = () => {
    const [inputValue, setInputValue] = useState('');
    const [showPicker, setShowPicker] = useState(false);
    const inputRef = useRef(null);

    const handleEmojiClick = (emojiObject, event) => {
        setInputValue(prevInput => prevInput + emojiObject.emoji);
        inputRef.current.focus(); // Keep focus on the input
    };

    const handleInputChange = (event) => {
        setInputValue(event.target.value);
    };

    return (
        // <div className="relative text-left w-full h-full flex flex-col items-center justify-center border p-4 border-slate-200 bg-white rounded-md shadow-md  bg-gradient-to-br from-rose-100 via-purple-200 to-purple-200">
        <div className='w-full flex flex-col h-full'>
        
            <h1 className="text-xl font-bold w-full text-center my-2">Viết bài đăng mới</h1>
            <article
        class="bg-white flex w-full h-fit flex-col items-center justify-center border-2 rounded-lg border-black p-4 shadow-[4px_4px_0_0_#000] transition-transform duration-500 ease-in-out transform hover:scale-101 shadow-[4px_4px_0_0_#000] transition-shadow duration-500 ease-in-out hover:shadow-[8px_8px_0_0_#000]"
        >

            <div className="flex items-center justify-center flex-col w-full relative space-y-4">
                <p className='font-bold text-xl'>Có gì mới?</p>
                <TextArea
                    ref={inputRef}
                    type="text"
                    value={inputValue}
                    onChange={handleInputChange}
                    placeholder="Type or select an emoji..."
                    style={{ height: 'fit-content',minHeight: '200px', width:'100%', resize: 'none' }}
                />
                <div className='flex justify-between items-center w-full flex-row mt-2'>
                    <div className='w-1/2 space-x-4'>
                        <Button shape="circle" onClick={() => setShowPicker(!showPicker)}>
                            <SmileOutlined />
                        </Button>
                        <Button shape="circle" onClick={() => alert('Upload image')}>
                            <FileImageOutlined />
                        </Button>
                    </div>
                    <div className='w-1/2 flex justify-end'>
                        <Button onClick={() => alert('Đăng bài')} style={{ width: '80%', display:'flex', justifyContent:'center', padding:'1.2rem 1rem' }}>
                            <p>Đăng bài</p>
                            <SendOutlined rotate={-45}/>
                        </Button>
                    </div>
                </div>
            </div>
            {showPicker && (
                <div className="mt-2 w-64 bg-white rounded-md shadow-lg z-10 absolute" style={{ left: '0', top: '300px', bottom: '0', right: '350px', margin: 'auto' }}>
                    <Picker onEmojiClick={handleEmojiClick} />
                </div>
            )}
            </article>
        </div>
       
    );
};

export default EmojiPickerInput;