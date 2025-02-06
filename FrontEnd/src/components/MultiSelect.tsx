import { useEffect, useState } from 'react';
import { Axios } from '../service/Axios.tsx';

const api = new Axios().getInstance();

const MultiSelect = ({ onChange }: { onChange: (selectedOptions: string[]) => void }) => {
    const [options, setOptions] = useState<string[]>([]);
    const [selectedOptions, setSelectedOptions] = useState<string[]>([]);

    useEffect(() => {
        const fetchOptions = async () => {
            try {
                const response = await api.get('/api/v1/demo/sectors');
                setOptions(response.data);
            } catch (error) {
                console.error('Error fetching options:', error);
            }
        };

        fetchOptions();
    }, []);

    const handleSelect = (option: string) => {
        setSelectedOptions([option]);
        onChange([option]);
    };

    return (
        <div className="mb-4">
            <label className="block text-gray-700 text-lg font-bold mb-2">Select Options</label>
            <select
                className="relative py-3 ps-4 pe-9 flex gap-x-2 text-nowrap w-full cursor-pointer bg-white border border-gray-200 rounded-lg text-start text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                value={selectedOptions[0] || ""}
                onChange={(e) => handleSelect(e.target.value)}
            >
                <option value="" disabled>Select an option</option>
                {options.map((option) => (
                    <option key={option} value={option}>
                        {option}
                    </option>
                ))}
            </select>
        </div>
    );
};

export default MultiSelect;