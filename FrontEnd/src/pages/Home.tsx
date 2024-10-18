import React,
{
    useState,
    useEffect
} from 'react';


function Home() {


  const [data, setData] = useState("default");

  useEffect(() => {
    async function fetchData() {
      try {
        const response = await fetch('http://localhost:8080/api/v1/demo/public');
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        const result = await response.json();
        setData(result);
      } catch (err) {
         console.log(err);
      }
    }

    fetchData();
  }, []);


  return (
    <>
      <div>{data.msg}</div>
    </>
  );
}

export default Home;
