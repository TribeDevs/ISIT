'use client'

import React from 'react';

const FileInput = ({ className = 'input-field', accept = '.jpg,.jpeg,.png' }) => {
    const handleFileChange = (event) => {
        const files = event.target.files;
    };

    return (
        <div>
            <label htmlFor="file-upload"></label>
            <input
                id="file-upload"
                type="file"
                className={className}
                accept={accept}
                onChange={handleFileChange}
            />
        </div>
    );
};

export default FileInput;