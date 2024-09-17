import React, { useState, useEffect } from 'react';
import axios from 'axios';

export default function App() {
  const [message, setMessage] = useState('');

  useEffect(() => {
    const fetchData = async () => {
      const result = await axios.get('http://localhost:8080/api/hello');
      setMessage(result.data);
    };
    fetchData();
  }, []);

        console.log(message);
  return (
    <>
      <h1 className="text-3xl font-bold underline">Hello world!</h1>
      <p className="text-3xl font-bold underline">{message}</p>
    </>
  );
}
