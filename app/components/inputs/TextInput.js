'use client'

import React, { useState, useEffect } from 'react';

const TextInput = ({ className = 'input-field', type = 'text', placeholder, value,onChange, name }) => {


    return (
        <div>
            <label htmlFor="text-input"></label>
            <input 
                type={type} 
                value={value} 
                onChange={onChange} 
                className={className} 
                placeholder={placeholder}
                name = {name}
            />
        </div>
    );
};

export default TextInput;