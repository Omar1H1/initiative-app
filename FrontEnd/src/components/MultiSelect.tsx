import { useEffect, useState } from 'react';
import { Axios } from '../service/Axios.tsx';

const api = new Axios().getInstance();

const MultiSelect = ({ onChange }: { onChange: (selectedOptions: string[]) => void }) => {
    const [options, setOptions] = useState<string[]>([]);
    const [selectedOptions, setSelectedOptions] = useState<string[]>([]);

    useEffect(() => {
        const fetchOptions = async () => {
            try {
                const response = await api.get('/api/v1/demo/sectors'); // Adjust the endpoint as needed
                setOptions(response.data);
            } catch (error) {
                console.error('Error fetching options:', error);
            }
        };

        fetchOptions();
    }, []);

    const handleSelect = (option: string) => {
        setSelectedOptions((prev) => {
            const newSelected = prev.includes(option) ? prev.filter((o) => o !== option) : [...prev, option];
            onChange(newSelected);
            return newSelected;
        });
    };

    return (
        <div className="mb-4">
            <label className="block text-gray-700 text-lg font-bold mb-2">Select Options</label>
            <select
                multiple
                className="hs-select-disabled:pointer-events-none hs-select-disabled:opacity-50 relative py-3 ps-4 pe-9 flex gap-x-2 text-nowrap w-full cursor-pointer bg-white border border-gray-200 rounded-lg text-start text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                value={selectedOptions}
                onChange={(e) => {
                    const selected = Array.from(e.target.selectedOptions, (option) => option.value);
                    setSelectedOptions(selected);
                    onChange(selected);
                }}
            >
                {options.map((option) => (
                    <option key={option} value={option} className="py-2 px-4 w-full text-sm text-gray-800 cursor-pointer hover:bg-gray-100 rounded-lg focus:outline-none focus:bg-gray-100">
                        {option}
                    </option>
                ))}
            </select>
        </div>
    );
};

export default MultiSelect;